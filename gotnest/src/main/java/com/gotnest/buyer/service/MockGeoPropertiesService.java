package com.gotnest.buyer.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
public class MockGeoPropertiesService {

    public Mono<Map<String, Object>> mockFeatureCollection(String bbox) {

        Map<String, Object> f1 = feature("prop-123", "GREEN", "$450K", "US$ 450,000", "901 Bagby St, Houston, TX 77002", "4bd | 3ba | 2,100 sqft", "https://cdn.gotnest.com/thumbs/prop-123-low.jpg", false, "Jay's Pick", -95.3698, 29.7604);
        Map<String, Object> f2 = feature("prop-456", "YELLOW", "$380K", "US$ 380,000", "600 Travis St, Houston, TX 77002", "3bd | 2ba | 1,600 sqft", "https://cdn.gotnest.com/thumbs/prop-456-low.jpg", true, null, -95.3657, 29.7601);

        Map<String, Object> f3 = feature("prop-457", "RED", "$520K", "US$ 520,000", "1200 Louisiana St, Houston, TX 77002", "2bd | 2ba | 1,350 sqft", "https://cdn.gotnest.com/thumbs/prop-457-low.jpg", false, null, -95.3692, 29.7574);
        Map<String, Object> f4 = feature("prop-458", "GREEN", "$299K", "US$ 299,000", "1001 McKinney St, Houston, TX 77002", "2bd | 1ba | 980 sqft", "https://cdn.gotnest.com/thumbs/prop-458-low.jpg", false, null, -95.3659, 29.7591);
        Map<String, Object> f5 = feature("prop-459", "YELLOW", "$650K", "US$ 650,000", "1400 Smith St, Houston, TX 77002", "3bd | 3ba | 2,350 sqft", "https://cdn.gotnest.com/thumbs/prop-459-low.jpg", true, "Jay's Pick", -95.3695, 29.7568);
        Map<String, Object> f6 = feature("prop-460", "RED", "$210K", "US$ 210,000", "1801 Main St, Houston, TX 77002", "1bd | 1ba | 720 sqft", "https://cdn.gotnest.com/thumbs/prop-460-low.jpg", false, null, -95.3690, 29.7485);
        Map<String, Object> f7 = feature("prop-461", "GREEN", "$875K", "US$ 875,000", "1 Hermann Park Dr, Houston, TX 77004", "4bd | 4ba | 3,200 sqft", "https://cdn.gotnest.com/thumbs/prop-461-low.jpg", true, null, -95.3896, 29.7180);
        Map<String, Object> f8 = feature("prop-462", "YELLOW", "$410K", "US$ 410,000", "Museum District, Houston, TX", "3bd | 2ba | 1,850 sqft", "https://cdn.gotnest.com/thumbs/prop-462-low.jpg", false, null, -95.3910, 29.7265);
        Map<String, Object> f9 = feature("prop-463", "RED", "$305K", "US$ 305,000", "Rice Village, Houston, TX", "2bd | 2ba | 1,100 sqft", "https://cdn.gotnest.com/thumbs/prop-463-low.jpg", false, null, -95.4166, 29.7183);
        Map<String, Object> f10 = feature("prop-464", "GREEN", "$950K", "US$ 950,000", "River Oaks Blvd, Houston, TX 77019", "5bd | 4ba | 3,800 sqft", "https://cdn.gotnest.com/thumbs/prop-464-low.jpg", true, "Jay's Pick", -95.4335, 29.7567);
        Map<String, Object> f11 = feature("prop-465", "YELLOW", "$530K", "US$ 530,000", "Montrose Blvd, Houston, TX 77006", "3bd | 3ba | 2,200 sqft", "https://cdn.gotnest.com/thumbs/prop-465-low.jpg", true, null, -95.3978, 29.7447);
        Map<String, Object> f12 = feature("prop-466", "RED", "$265K", "US$ 265,000", "Washington Ave, Houston, TX 77007", "2bd | 1ba | 980 sqft", "https://cdn.gotnest.com/thumbs/prop-466-low.jpg", false, null, -95.3902, 29.7695);
        Map<String, Object> f13 = feature("prop-467", "GREEN", "$720K", "US$ 720,000", "The Heights, Houston, TX 77008", "4bd | 3ba | 2,600 sqft", "https://cdn.gotnest.com/thumbs/prop-467-low.jpg", false, null, -95.4030, 29.7925);
        Map<String, Object> f14 = feature("prop-468", "YELLOW", "$485K", "US$ 485,000", "Midtown, Houston, TX 77002", "3bd | 2ba | 1,900 sqft", "https://cdn.gotnest.com/thumbs/prop-468-low.jpg", true, null, -95.3770, 29.7410);
        Map<String, Object> f15 = feature("prop-469", "RED", "$335K", "US$ 335,000", "EaDo, Houston, TX 77003", "2bd | 2ba | 1,200 sqft", "https://cdn.gotnest.com/thumbs/prop-469-low.jpg", false, null, -95.3505, 29.7525);
        Map<String, Object> f16 = feature("prop-470", "GREEN", "$1.2M", "US$ 1,200,000", "West University Place, Houston, TX 77005", "5bd | 4ba | 4,100 sqft", "https://cdn.gotnest.com/thumbs/prop-470-low.jpg", true, "Jay's Pick", -95.4320, 29.7189);
        Map<String, Object> f17 = feature("prop-471", "YELLOW", "$420K", "US$ 420,000", "Galleria, Houston, TX 77056", "3bd | 2ba | 1,850 sqft", "https://cdn.gotnest.com/thumbs/prop-471-low.jpg", false, null, -95.4641, 29.7399);
        Map<String, Object> f18 = feature("prop-472", "RED", "$295K", "US$ 295,000", "Mid West, Houston, TX 77063", "2bd | 2ba | 1,150 sqft", "https://cdn.gotnest.com/thumbs/prop-472-low.jpg", false, null, -95.5220, 29.7368);
        Map<String, Object> f19 = feature("prop-473", "GREEN", "$680K", "US$ 680,000", "Memorial, Houston, TX 77024", "4bd | 3ba | 2,500 sqft", "https://cdn.gotnest.com/thumbs/prop-473-low.jpg", true, null, -95.5170, 29.7733);
        Map<String, Object> f20 = feature("prop-474", "YELLOW", "$390K", "US$ 390,000", "Spring Branch, Houston, TX 77080", "3bd | 2ba | 1,700 sqft", "https://cdn.gotnest.com/thumbs/prop-474-low.jpg", false, null, -95.5448, 29.8146);
        Map<String, Object> f21 = feature("98125197", "GREEN", "$2.6K", "US$ 2,600", "727 Bunker Hill Road 38, Houston, TX 77024", "2bd | 3ba | 1,796 sqft", "https://cdn.gotnest.com/thumbs/98125197.jpg", false, null, -95.532161, 29.774177);
        Map<String, Object> f22 = feature("6496396", "YELLOW", "$45K", "US$ 45,000", "12500 Sandpiper Drive 93, Houston, TX 77035", "2bd | 2ba | 1,140 sqft", "https://cdn.gotnest.com/thumbs/6496396.jpg", false, null, -95.503674, 29.640616);
        Map<String, Object> f23 = feature("20588236", "RED", "$46.9K", "US$ 46,990", "5625 Antoine Dr Drive 822, Houston, TX 77091", "2bd | 2ba | 1,027 sqft", "https://cdn.gotnest.com/thumbs/20588236.jpg", false, null, -95.474177, 29.847494);
        Map<String, Object> f24 = feature("prop-477", "GREEN", "$800K", "US$ 800,000", "7700 Woodway Dr, Houston, TX 77063", "4bd | 3ba | 3,000 sqft", "https://cdn.gotnest.com/thumbs/prop-477-low.jpg", true, null, -95.5234, 29.7356);
        Map<String, Object> f25 = feature("prop-478", "YELLOW", "$360K", "US$ 360,000", "8500 Westheimer Rd, Houston, TX 77063", "3bd | 2ba | 1,700 sqft", "https://cdn.gotnest.com/thumbs/prop-478-low.jpg", false, null, -95.5371, 29.7334);
        Map<String, Object> f26 = feature("prop-479", "RED", "$290K", "US$ 290,000", "9000 Richmond Ave, Houston, TX 77063", "2bd | 2ba | 1,200 sqft", "https://cdn.gotnest.com/thumbs/prop-479-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f27 = feature("prop-480", "GREEN", "$1.1M", "US$ 1,100,000", "6800 San Felipe St, Houston, TX 77063", "5bd | 4ba | 4,500 sqft", "https://cdn.gotnest.com/thumbs/prop-480-low.jpg", true, "Jay's Pick", -95.4961, 29.7356);
        Map<String, Object> f28 = feature("prop-481", "YELLOW", "$410K", "US$ 410,000", "7700 W Little York Rd, Houston, TX 77040", "3bd | 2ba | 1,800 sqft", "https://cdn.gotnest.com/thumbs/prop-481-low.jpg", false, null, -95.5251, 29.8356);
        Map<String, Object> f29 = feature("prop-482", "RED", "$310K", "US$ 310,000", "8500 W Little York Rd, Houston, TX 77040", "2bd | 2ba | 1,250 sqft", "https://cdn.gotnest.com/thumbs/prop-482-low.jpg", false, null, -95.5251, 29.8056);
        Map<String, Object> f30 = feature("prop-483", "GREEN", "$1.3M", "US$ 1,300,000", "6000 Woodway Dr, Houston, TX 77057", "5bd | 4ba | 4,800 sqft", "https://cdn.gotnest.com/thumbs/prop-483-low.jpg", true, "Jay's Pick", -95.5234, 29.7356);
        Map<String, Object> f31 = feature("prop-484", "YELLOW", "$450K", "US$ 450,000", "10000 Richmond Ave, Houston, TX 77042", "3bd | 2ba | 1,900 sqft", "https://cdn.gotnest.com/thumbs/prop-484-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f32 = feature("prop-485", "RED", "$350K", "US$ 350,000", "11000 Westheimer Rd, Houston, TX 77042", "2bd | 2ba | 1,400 sqft", "https://cdn.gotnest.com/thumbs/prop-485-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f33 = feature("prop-486", "GREEN", "$1.4M", "US$ 1,400,000", "5000 San Felipe St, Houston, TX 77056", "5bd | 4ba | 5,000 sqft", "https://cdn.gotnest.com/thumbs/prop-486-low.jpg", true, "Jay's Pick", -95.4961, 29.7356);
        Map<String, Object> f34 = feature("prop-487", "YELLOW", "$480K", "US$ 480,000", "3000 W Loop N, Houston, TX 77092", "3bd | 2ba | 2,000 sqft", "https://cdn.gotnest.com/thumbs/prop-487-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f35 = feature("prop-488", "RED", "$370K", "US$ 370,000", "4000 W Loop N, Houston, TX 77092", "2bd | 2ba | 1,500 sqft", "https://cdn.gotnest.com/thumbs/prop-488-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f36 = feature("prop-489", "GREEN", "$1.5M", "US$ 1,500,000", "2000 Woodway Dr, Houston, TX 77057", "5bd | 4ba | 5,500 sqft", "https://cdn.gotnest.com/thumbs/prop-489-low.jpg", true, "Jay's Pick", -95.5234, 29.7356);
        Map<String, Object> f37 = feature("prop-490", "YELLOW", "$500K", "US$ 500,000", "1500 Richmond Ave, Houston, TX 77042", "3bd | 2ba | 2,200 sqft", "https://cdn.gotnest.com/thumbs/prop-490-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f38 = feature("prop-491", "RED", "$400K", "US$ 400,000", "2500 W Loop N, Houston, TX 77092", "2bd | 2ba | 1,800 sqft", "https://cdn.gotnest.com/thumbs/prop-491-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f39 = feature("prop-492", "GREEN", "$1.6M", "US$ 1,600,000", "1000 San Felipe St, Houston, TX 77056", "5bd | 4ba | 6,000 sqft", "https://cdn.gotnest.com/thumbs/prop-492-low.jpg", true, "Jay's Pick", -95.4961, 29.7356);
        Map<String, Object> f40 = feature("prop-493", "YELLOW", "$550K", "US$ 550,000", "3500 W Loop N, Houston, TX 77092", "3bd | 2ba | 2,500 sqft", "https://cdn.gotnest.com/thumbs/prop-493-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f41 = feature("prop-494", "RED", "$450K", "US$ 450,000", "4500 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,000 sqft", "https://cdn.gotnest.com/thumbs/prop-494-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f42 = feature("prop-495", "GREEN", "$1.7M", "US$ 1,700,000", "2000 Woodway Dr, Houston, TX 77057", "5bd | 4ba | 6,500 sqft", "https://cdn.gotnest.com/thumbs/prop-495-low.jpg", true, "Jay's Pick", -95.5234, 29.7356);
        Map<String, Object> f43 = feature("prop-496", "YELLOW", "$600K", "US$ 600,000", "3000 W Loop N, Houston, TX 77092", "3bd | 2ba | 2,800 sqft", "https://cdn.gotnest.com/thumbs/prop-496-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f44 = feature("prop-497", "RED", "$500K", "US$ 500,000", "4000 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,300 sqft", "https://cdn.gotnest.com/thumbs/prop-497-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f45 = feature("prop-498", "GREEN", "$1.8M", "US$ 1,800,000", "6000 San Felipe St, Houston, TX 77056", "5bd | 4ba | 7,000 sqft", "https://cdn.gotnest.com/thumbs/prop-498-low.jpg", true, "Jay's Pick", -95.4961, 29.7356);
        Map<String, Object> f46 = feature("prop-499", "YELLOW", "$650K", "US$ 650,000", "7000 W Loop N, Houston, TX 77092", "3bd | 2ba | 3,000 sqft", "https://cdn.gotnest.com/thumbs/prop-499-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f47 = feature("prop-500", "RED", "$550K", "US$ 550,000", "8000 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,500 sqft", "https://cdn.gotnest.com/thumbs/prop-500-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f48 = feature("prop-501", "GREEN", "$1.9M", "US$ 1,900,000", "9000 Woodway Dr, Houston, TX 77057", "5bd | 4ba | 7,500 sqft", "https://cdn.gotnest.com/thumbs/prop-501-low.jpg", true, "Jay's Pick", -95.5234, 29.7356);
        Map<String, Object> f49 = feature("prop-502", "YELLOW", "$700K", "US$ 700,000", "10000 W Loop N, Houston, TX 77092", "3bd | 2ba | 3,200 sqft", "https://cdn.gotnest.com/thumbs/prop-502-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f50 = feature("prop-503", "RED", "$600K", "US$ 600,000", "11000 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,800 sqft", "https://cdn.gotnest.com/thumbs/prop-503-low.jpg", false, null, -95.5371, 29.6734);

        List<Map<String, Object>> features = List.of(
                f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12,
                f13, f14, f15, f16, f17, f18, f19, f20,
                f21, f22, f23, f24, f25, f26, f27, f28, f29, f30,
                f31, f32, f33, f34, f35, f36, f37, f38, f39, f40,
                f41, f42, f43, f44, f45, f46, f47, f48, f49, f50
        );

        Map<String, Object> featureCollection = Map.of(
                "type", "FeatureCollection",
                "features", features
        );

        return Mono.just(featureCollection);
    }

    private Map<String, Object> feature(String id,
                                        String pinColor,
                                        String priceShort,
                                        String fullPrice,
                                        String address,
                                        String specs,
                                        String imageUrl,
                                        boolean isFavorited,
                                        String badge,
                                        double lon,
                                        double lat) {
        Map<String, Object> geometry = Map.of(
                "type", "Point",
                "coordinates", List.of(lon, lat)
        );
        // Parse specs string (e.g., "2bd | 2ba | 1,100 sqft")
        String[] specsParts = specs.split("\\|");
        String bedrooms = specsParts.length > 0 ? specsParts[0].trim() : "";
        String bathrooms = specsParts.length > 1 ? specsParts[1].trim() : "";
        String area = specsParts.length > 2 ? specsParts[2].trim() : "";
        Map<String, Object> cardData = badge == null ?
                Map.of(
                        "fullPrice", fullPrice,
                        "address", address,
                        "bedrooms", bedrooms,
                        "bathrooms", bathrooms,
                        "area", area,
                        "imageUrl", imageUrl,
                        "isFavorited", isFavorited
                ) :
                Map.of(
                        "fullPrice", fullPrice,
                        "address", address,
                        "bedrooms", bedrooms,
                        "bathrooms", bathrooms,
                        "area", area,
                        "imageUrl", imageUrl,
                        "isFavorited", isFavorited,
                        "badge", badge
                );
        Map<String, Object> properties = Map.of(
                "id", id,
                "pinColor", pinColor,
                "cluster", false,
                "priceShort", priceShort,
                "cardData", cardData
        );
        return Map.of(
                "type", "Feature",
                "geometry", geometry,
                "properties", properties
        );
    }
}
