# **Especificação Técnica: Sincronia BFF & UX (Manual \+ Chat)**

[MapBox Java \- Git](https://github.com/mapbox/mapbox-java) (gson e nao jackson)  
[MapBox \- MVN](https://mvnrepository.com/artifact/com.mapbox.mapboxsdk/mapbox-sdk-geojson/5.8.0)

**Projeto:** Got Nest? **Foco:** Interação Frontend \<-\> BFF **Versão:** 3.1 (Fluxos Detalhados \+ Diagramas \+ Contratos)

## **1\. O Modelo Mental: "Dois Gatilhos, Um Resultado"**

O mapa e a lista de imóveis não são estáticos. Eles reagem a **dois tipos de gatilhos** diferentes, mas consomem a **mesma lógica** no Backend.

### **Modo A: Exploração Manual (A "Mão")**

* **Cenário:** O usuário não está conversando. Ele está arrastando o mapa, dando zoom ou aplicando filtros manuais (se houver).  
* **Comportamento:** O mapa deve carregar imóveis conforme o usuário navega, mas **respeitando silenciosamente** o perfil que o Jay já definiu (ex: se o usuário disse antes que odeia andar térreo, o mapa manual não deve mostrar andares térreos, a menos que ele limpe o filtro).

### **Modo B: Sincronia via Chat (O "Comando")**

* **Cenário:** O usuário fala com o Jay. "Mostre-me opções mais baratas".  
* **Comportamento:** O Chat processa a intenção, atualiza o contexto no servidor e **manda o Frontend atualizar o mapa**. O usuário vê os pinos mudarem de cor ou posição sem ter tocado no mapa.

## **2\. Fluxo de Sincronização (Passo a Passo Detalhado?)**

### **Fluxo 1: O Usuário Move o Mapa (Manual)**

Este é o fluxo "passivo". O usuário está no controle da navegação geográfica, mas o conteúdo é disponibilizazdo pelo backend.

**Diagrama de Sequência Textual (Fluxo Manual):**

\[Usuário\]   \-\> (Move Mapa) 	\-\> \[Frontend: Map Component\]

\[Frontend\] \-\> (Detecta Parada) \-\> \[Frontend: Controller\]

\[Frontend\] \-\> GET /geo-search?bbox=... \-\> \[BFF\]

\[BFF\]        \-\> (Lê Contexto/Sessão) \-\> \[Context Service\]

\[BFF\] 	     \-\> (Busca Imóveis no Bbox \+ Filtros Contexto) \-\> \[Database\]

\[BFF\]        \-\> (Calcula Cores/Match) \-\> \[BFF Logic\]

\[BFF\]        \-\> JSON FeatureCollection \-\> \[Frontend\]

\[Frontend\] \-\> (Renderiza Pinos) \-\> \[Usuário\]

**Passo a Passo Técnico:**

1. **Interação do Usuário:** O usuário faz *pan* ou *zoom* no mapa para explorar uma nova área.  
2. **Debounce (Frontend):** O Frontend deve aguardar o evento onRegionChangeComplete (ou similar) e aplicar um pequeno *debounce* (ex: 300ms(validar…)) para evitar chamadas excessivas enquanto o usuário ainda está arrastando.  
3. **Request (BFF):** O Frontend dispara GET /geo-search enviando **apenas** o bbox (coordenadas minLon,minLat,maxLon,maxLat) da área visível. Nenhum filtro de preferência é enviado aqui; o front assume que o back já sabe.  
4. **Processamento de Contexto (Backend):**  
   * O BFF recebe o request.  
   * Identifica o usuário/sessão.  
   * Recupera o "Estado Atual do Jay" (ex: "Usuário quer casas com \> 3 quartos e piscina"). Muito provavelmente vamos precisar do ID do usuário para poder pegar o perfil que está armazenado com Ricardo  
5. **Query Espacial e Filtragem:**  
   * O Backend busca no banco espacial todos os imóveis dentro do bbox.  
   * Aplica os filtros do contexto (ex: remove casas sem piscina).  
6. **Enriquecimento Visual (Lógica de Cores):**  
   * Para os imóveis restantes, o BFF calcula o pinColor:  
     * **GREEN:** Imóvel perfeito (Match alto com preferências \+ Orçamento OK).  
     * **YELLOW:** Imóvel bom, mas foge um pouco (ex: preço 10% acima ou falta 1 feature secundária).  
     * **RED:** Imóvel disponível, mas fora do perfil (ex: muito caro). *Decisão de Design:* Podemos optar por limitar as extremamente acima do budget do usuário, ex.:  
       1. Definir máximo do usuário ValorMaximoUsuario \= ValorOrcamentoUsuario \* 0.25 (propriedades 25% mais caras)  
       2. Se ListPrice \> ValorMaximoUsuario traz apenas algumas.. Apesar que pensando aqui, não faz sentido gastarmos com isso não é? Mas é requisito…  
7. **Response:** Retorna o FeatureCollection.  
8. **Renderização:** O componente de mapa do Frontend atualiza os pinos. Se houver clusters, o SDK do Mapbox recalcula.

### **Fluxo 2: O Usuário Fala com o Jay (Sincronia Ativa)**

Aqui o Chat atua como um "controle remoto" da interface. O usuário comanda via texto, e a UI reage.

**Diagrama de Sequência Textual (Fluxo Chat):**

\[Usuário\] \-\> "Quero ver casas com piscina" \-\> \[Frontend: Chat UI\]

\[Frontend\] \-\> POST /chat/message \-\> \[BFF\]

\[BFF\] \-\> (Processa NLP/Intenção) \-\> \[AI Service\]

\[AI Service\] \-\> (Atualiza Preferências: \+Pool) \-\> \[Context Service\]

\[AI Service\] \-\> (Gera Resposta Texto \+ Comandos UI) \-\> \[BFF\]

\[BFF\] \-\> JSON { reply, synchronization } \-\> \[Frontend\]

\[Frontend\] \-\> (Exibe Texto do Jay) \-\> \[Chat UI\]

\[Frontend\] \-\> (Lê 'shouldRefreshMap') \-\> \[Map Controller\]

\[Map Controller\] \-\> (Captura Bbox Atual) \-\> \[Frontend\]

\[Map Controller\] \-\> GET /geo-search?bbox=... \-\> \[BFF\]

... (Segue Fluxo 1 para atualizar pinos) ...

**Passo a Passo Técnico:**

1. **Envio da Mensagem:** O usuário digita ou fala uma preferência.  
2. **Processamento de Intenção (Backend):**  
   * A LLM por meio do BFF identifica que a mensagem alterou os critérios de busca (ex: adicionou filtro "Pool").  
   * **Ação Crítica:** O Backend salva essa nova preferência no perfil de sessão do usuário no banco. Agora, o "Contexto Atual" inclui "Tem Piscina \= true".  
3. **Construção da Resposta (Backend):**  
   * O Backend gera a resposta textual do Jay ("Entendido, filtrei por piscinas...").  
   * O Backend decide que essa mudança exige uma atualização visual.  
   * Adiciona o objeto synchronization na resposta com shouldRefreshMap: true.  
4. **Reação do Frontend (O Gatilho):**  
   * Ao receber a resposta, o Frontend mostra a mensagem no balão.  
   * Um "Observer" ou "Effect" no código do Frontend detecta shouldRefreshMap: true.  
5. **Execução da Sincronia:**  
   * O Frontend **automaticamente** dispara a função de recarregar o mapa (a mesma usada no Fluxo 1).  
   * Ele usa as coordenadas **atuais** da tela (onde o usuário já estava olhando).  
6. **Atualização Visual:**  
   * O request GET /geo-search bate no Backend.  
   * O Backend (agora ciente do filtro "Piscina") retorna apenas as casas com piscina naquele mesmo bbox.  
   * O usuário vê os pinos antigos sumirem e novos aparecerem (ou mudarem de cor), validando que o Jay "entendeu e agiu".

## **3\. Especificação dos Endpoints (Contrato)**

### **A. Busca Geo-Contextual (O Consumidor Passivo)**

Usado tanto no movimento manual quanto no "refresh" do chat.

* **Rota:** GET /bff/v1/properties/geo-search  
* **Parâmetros:** bbox (Obrigatório).

**Response ([GeoJSON \- RFC 7946](https://datatracker.ietf.org/doc/html/rfc7946)):** O Backend manda tudo pronto. O Front não decide cor nem formatação.

| {  "type": "FeatureCollection",  "features": \[    {      "type": "Feature",      "geometry": {        "type": "Point",        "coordinates": \[-95.3698, 29.7604\] // Lon, Lat (Invertido para Mapbox)      },      "properties": {        // \--- Dados para o Marcador no Mapa \---        "id": "prop-123",        "pinColor": "GREEN", // Logica do Backend: Match Perfeito        "cluster": false,        "priceShort": "$450K",         // \--- Dados para o Carrossel (Zero Latency) \---        "cardData": {             "fullPrice": "US$ 450,000",             "address": "123 Quiet Lane, Houston",             "specs": "4bd | 3ba | 2,100 sqft",             "imageUrl": "\[https://cdn.gotnest.com/thumbs/prop-123-low.jpg\](https://cdn.gotnest.com/thumbs/prop-123-low.jpg)",             "isFavorited": false,             "badge": "Jay's Pick" // Opcional: Badge especial de recomendação        }      }    }  \]} |
| :---- |

### **B. Chat (O Agente de Mudança)**

O endpoint que altera o contexto e comanda a UI.

* **Rota:** POST /bff/v1/chat/message  
* **Body:** { "message": "Quero ver casas com piscina" }

**Response (Payload Híbrido: Texto \+ Comandos):**

| {  // 1. A Resposta Visual do Chat (Texto)  "reply": {    "text": "Entendido. Filtrei o mapa para mostrar apenas casas com piscina nesta área.",    "sender": "JAY",    "timestamp": "2025-10-20T10:00:00Z"  },    // 2. O Comando Técnico para o Frontend  "synchronization": {    // Flag Bool: Se true, o Front DEVE chamar GET /geo-search imediatamente    "shouldRefreshMap": true,         // Objeto Opcional: Se presente, o Front move a câmera antes de buscar    "cameraMove": {       "center": \[\-95.3698, 29.7604\],      "zoom": 13    },    // Objeto Opcional: Se presente, abre o carrossel num imóvel específico    "selectPropertyId": "prop-123"  }} |
| :---- |

## **4\. Resumo de Responsabilidades (Checklist)**

### **Para o Frontend (Mobile)**

* \[ \] **Implementar Mapa "Burro":** Renderizar estritamente o que vem do /geo-search. Não tentar filtrar o GeoJSON localmente.  
* \[ \] **Inverter Coordenadas:** Garantir que ao ler o clique do usuário, envie bbox no formato correto.  
* \[ \] **Escuta Ativa (Watcher):** Criar um useEffect (ou equivalente) que monitora a resposta do /chat/message. Se vier shouldRefreshMap: true, invocar a função de refresh do mapa.  
* \[ \] **Zero Latency:** Ao clicar no pino, usar o feature.properties.cardData para abrir o carrossel. Não fazer fetch extra.

### **Para o Backend (BFF)**

* \[ \] **Gestão de Contexto:** Garantir que o filtro aplicado pelo Chat ("tem piscina") persista na sessão do usuário para que as próximas chamadas de /geo-search (feitas manualmente pelo usuário) continuem respeitando esse filtro.  
* \[ \] **Lógica de Cores:** Implementar a regra de negócio (Orçamento vs Preço) para definir GREEN, YELLOW, RED antes de gerar o JSON.  
* \[ \] **Formatação:** Entregar preços e specs já formatados como strings ($450K, 3bd | 2ba).

## **5\. Notas Finais e Erros Comuns**

1. **A "Armadilha" do Refresh:** O Frontend deve garantir que o shouldRefreshMap use as coordenadas **atuais** da tela. Um erro comum é usar coordenadas antigas ou padrão, fazendo o mapa "pular" para longe de onde o usuário estava olhando.  
2. **Concorrência:** Se o usuário mover o mapa *enquanto* o Jay está respondendo, a chamada manual (onRegionChange) e a chamada automática (shouldRefreshMap) podem colidir. Implementar cancelamento de request anterior (AbortController) no Frontend é recomendado.  
3. **Persistência do Filtro:** O Backend deve manter o filtro ativo até que o usuário (ou o Jay) o limpe explicitamente (ex: "Mostrar todas as casas").  
4.  O Conflito "Jackson vs Gson" (Técnico)  
* **O Problema:** O Spring Boot (framework do Backend) usa **Jackson** por padrão para gerar JSON. O SDK do Mapbox usa **Gson** (outra biblioteca, do Google).  
* **O Risco:** Se o Backend tentar retornar um objeto `FeatureCollection` do Mapbox diretamente no Controller do Spring, o Jackson pode não saber serializar isso corretamente, ou o objeto pode sair vazio/estranho.  
* **A Solução:** O Backend terá que configurar um *bean* conversor ou simplesmente gerar a string JSON usando o método `.toJson()` do próprio SDK do Mapbox antes de devolver a resposta.  
  