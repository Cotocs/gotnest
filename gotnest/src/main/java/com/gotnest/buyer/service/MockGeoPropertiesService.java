package com.gotnest.buyer.service;

import com.gotnest.buyer.dto.PropertySearchFilterDTO;
import com.gotnest.buyer.exception.AddressNotFoundException;
import com.gotnest.buyer.exception.NotAvailableLocationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;


@Service
public class MockGeoPropertiesService {
    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURES = "features";
    private static final String ERROR = "error";

    private static final double HOUSTON_MIN_LON = -95.7; // west
    private static final double HOUSTON_MIN_LAT = 29.6;  // south
    private static final double HOUSTON_MAX_LON = -95.1; // east
    private static final double HOUSTON_MAX_LAT = 29.95; // north

    public Mono<Map<String, Object>> filterProperties(PropertySearchFilterDTO filter) {
        Map<String, Object> all = mockFeatureCollection().block();
        if (all == null) return Mono.just(emptyFeatureCollection());
        List<Map<String, Object>> features = getFeatures(all);
        List<Map<String, Object>> filtered = features.stream()
                .filter(f -> matchesFilter(f, filter))
                .toList();
        return Mono.just(featureCollection(filtered));
    }

    public Mono<Map<String, Object>> mockFeatureCollection() {

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
        Map<String, Object> f46 = feature("prop-499", "YELLOW", "$650K", "US$ 650,000", "7000 W Loop N, Houston, TX 77092", "9bd | 2ba | 3,000 sqft", "https://cdn.gotnest.com/thumbs/prop-499-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f47 = feature("prop-500", "RED", "$550K", "US$ 550,000", "8000 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,500 sqft", "https://cdn.gotnest.com/thumbs/prop-500-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f48 = feature("prop-501", "GREEN", "$1.9M", "US$ 1,900,000", "9000 Woodway Dr, Houston, TX 77057", "5bd | 4ba | 7,500 sqft", "https://cdn.gotnest.com/thumbs/prop-501-low.jpg", true, "Jay's Pick", -95.5234, 29.7356);
        Map<String, Object> f49 = feature("prop-502", "YELLOW", "$700K", "US$ 700,000", "10000 W Loop N, Houston, TX 77092", "3bd | 2ba | 3,200 sqft", "https://cdn.gotnest.com/thumbs/prop-502-low.jpg", false, null, -95.5371, 29.7034);
        Map<String, Object> f50 = feature("prop-503", "RED", "$600K", "US$ 600,000", "11000 Richmond Ave, Houston, TX 77042", "2bd | 2ba | 2,800 sqft", "https://cdn.gotnest.com/thumbs/prop-503-low.jpg", false, null, -95.5371, 29.6734);
        Map<String, Object> f101 = feature("91912354", "GREEN", "$175K", "US$ 175,000", "2212 Shadowdale Drive 358, Houston, TX 77043", "3bd | 3ba | 1,964 sqft", "", false, null, -95.554112, 29.813336);
        Map<String, Object> f102 = feature("88653053", "YELLOW", "$250K", "US$ 250,000", "6219 Reed Road, Houston, TX 77087", "4bd | 3ba | 1,820 sqft", "", false, null, -95.314333, 29.65993);
        Map<String, Object> f103 = feature("84072393", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f104 = feature("84072394", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f105 = feature("84072395", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f106 = feature("84072396", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f107 = feature("84072397", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f108 = feature("84072398", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f109 = feature("84072399", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f110 = feature("84072400", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f111 = feature("84072401", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f112 = feature("84072402", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f113 = feature("84072403", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f114 = feature("84072404", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f115 = feature("84072405", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f116 = feature("84072406", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f117 = feature("84072407", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f118 = feature("84072408", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f119 = feature("84072409", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f120 = feature("84072410", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f121 = feature("84072411", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f122 = feature("84072412", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f123 = feature("84072413", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f124 = feature("84072414", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f125 = feature("84072415", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f126 = feature("84072416", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f127 = feature("84072417", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f128 = feature("84072418", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f129 = feature("84072419", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f130 = feature("84072420", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f131 = feature("84072421", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f132 = feature("84072422", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f133 = feature("84072423", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f134 = feature("84072424", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f135 = feature("84072425", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f136 = feature("84072426", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f137 = feature("84072427", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f138 = feature("84072428", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f139 = feature("84072429", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f140 = feature("84072430", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f141 = feature("84072431", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f142 = feature("84072432", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f143 = feature("84072433", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f144 = feature("84072434", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f145 = feature("84072435", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f146 = feature("84072436", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f147 = feature("84072437", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f148 = feature("84072438", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f149 = feature("84072439", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f150 = feature("84072440", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f151 = feature("84072441", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f152 = feature("84072442", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f153 = feature("84072443", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f154 = feature("84072444", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f155 = feature("84072445", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f156 = feature("84072446", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f157 = feature("84072447", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f158 = feature("84072448", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f159 = feature("84072449", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f160 = feature("84072450", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f161 = feature("84072451", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f162 = feature("84072452", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f163 = feature("84072453", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f164 = feature("84072454", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f165 = feature("84072455", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f166 = feature("84072456", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f167 = feature("84072457", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f168 = feature("84072458", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f169 = feature("84072459", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f170 = feature("84072460", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f171 = feature("84072461", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f172 = feature("84072462", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f173 = feature("84072463", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f174 = feature("84072464", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f175 = feature("84072465", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f176 = feature("84072466", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f177 = feature("84072467", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f178 = feature("84072468", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f179 = feature("84072469", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f180 = feature("84072470", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f181 = feature("84072471", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f182 = feature("84072472", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f183 = feature("84072473", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f184 = feature("84072474", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f185 = feature("84072475", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f186 = feature("84072476", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f187 = feature("84072477", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f188 = feature("84072478", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f189 = feature("84072479", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f190 = feature("84072480", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f191 = feature("84072481", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f192 = feature("84072482", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f193 = feature("84072483", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f194 = feature("84072484", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f195 = feature("84072485", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f196 = feature("84072486", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f197 = feature("84072487", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f198 = feature("84072488", "YELLOW", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f199 = feature("84072489", "RED", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);
        Map<String, Object> f200 = feature("84072490", "GREEN", "$---", "US$ ---", "410 Yale Oaks Ln, Houston, TX", "3bd | 3ba | 2,132 sqft", "", false, null, -95.405064, 29.813336);

        List<Map<String, Object>> features = List.of(
                f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12,
                f13, f14, f15, f16, f17, f18, f19, f20,
                f21, f22, f23, f24, f25, f26, f27, f28, f29, f30,
                f31, f32, f33, f34, f35, f36, f37, f38, f39, f40,
                f41, f42, f43, f44, f45, f46, f47, f48, f49, f50,
                f101, f102, f103, f104, f105, f106, f107, f108, f109, f110, f111, f112,
                f113, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124,
                f125, f126, f127, f128, f129, f130, f131, f132, f133, f134, f135, f136,
                f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147, f148,
                f149, f150, f151, f152, f153, f154, f155, f156, f157, f158, f159, f160,
                f161, f162, f163, f164, f165, f166, f167, f168, f169, f170, f171, f172,
                f173, f174, f175, f176, f177, f178, f179, f180, f181, f182, f183, f184,
                f185, f186, f187, f188, f189, f190, f191, f192, f193, f194, f195, f196,
                f197, f198, f199, f200
        );

        return Mono.just(featureCollection(features));
    }

    public Mono<Map<String, Object>> searchByAddress(String query) {
        Map<String, Object> all = mockFeatureCollection().block();
        if (all == null) return Mono.just(emptyFeatureCollection());
        List<Map<String, Object>> features = getFeatures(all);
        List<Map<String, Object>> filtered = features.stream()
                .filter(f -> addressContains(f, query))
                .toList();
        if (filtered.isEmpty()) {
            throw new AddressNotFoundException();
        }
        return Mono.just(featureCollection(filtered));
    }

    public Mono<Map<String, Object>> searchByAddressWithBboxAndCount(String query, String bbox) {
        if (bbox != null && !bbox.isBlank()) {
            String[] parts = bbox.split(",");
            if (parts.length == 4) {
                try {
                    double minLon = Double.parseDouble(parts[0].trim());
                    double minLat = Double.parseDouble(parts[1].trim());
                    double maxLon = Double.parseDouble(parts[2].trim());
                    double maxLat = Double.parseDouble(parts[3].trim());

                    boolean intersects = !(maxLon < HOUSTON_MIN_LON || minLon > HOUSTON_MAX_LON ||
                            maxLat < HOUSTON_MIN_LAT || minLat > HOUSTON_MAX_LAT);
                    if (!intersects) {
                        throw new NotAvailableLocationException();
                    }
                } catch (NumberFormatException ex) {
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY,
                            "Invalid bbox format.");
                }
            } else {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY,
                        "Invalid bbox format.");
            }
        }
        // Realiza a busca por endereço e adiciona bbox+count baseado nos recursos
        return searchByAddress(query)
            .map(featureCollectionOrig -> {
                var featureCollection = new java.util.HashMap<>(featureCollectionOrig);
                java.util.List<java.util.Map<String, Object>> features = getFeatures(featureCollection);
                double minLon = Double.POSITIVE_INFINITY, minLat = Double.POSITIVE_INFINITY;
                double maxLon = Double.NEGATIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
                for (var feature : features) {
                    java.util.Map<String, Object> geometry = getGeometry(feature);
                    java.util.List<?> coords = (java.util.List<?>) geometry.get("coordinates");
                    double lon = ((Number) coords.get(0)).doubleValue();
                    double lat = ((Number) coords.get(1)).doubleValue();
                    if (lon < minLon) minLon = lon;
                    if (lon > maxLon) maxLon = lon;
                    if (lat < minLat) minLat = lat;
                    if (lat > maxLat) maxLat = lat;
                }
                if (!features.isEmpty()) {
                    featureCollection.put("bbox", java.util.List.of(minLon, minLat, maxLon, maxLat));
                }
                featureCollection.put("count", features.size());
                return featureCollection;
            });
    }

    public Mono<Map<String, Object>> searchByAddressWithFilters(String query, com.gotnest.buyer.dto.PropertySearchFilterDTO filter) {
        return searchByAddress(query)
            .map(featureCollectionOrig -> {
                var featureCollection = new java.util.HashMap<>(featureCollectionOrig);
                java.util.List<java.util.Map<String, Object>> features = getFeatures(featureCollection);
                java.util.List<java.util.Map<String, Object>> filtered = features.stream()
                        .filter(f -> matchesFilter(f, filter))
                        .toList();

                double minLon = Double.POSITIVE_INFINITY, minLat = Double.POSITIVE_INFINITY;
                double maxLon = Double.NEGATIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
                for (var feature : filtered) {
                    java.util.Map<String, Object> geometry = getGeometry(feature);
                    java.util.List<?> coords = (java.util.List<?>) geometry.get("coordinates");
                    double lon = ((Number) coords.get(0)).doubleValue();
                    double lat = ((Number) coords.get(1)).doubleValue();
                    if (lon < minLon) minLon = lon;
                    if (lon > maxLon) maxLon = lon;
                    if (lat < minLat) minLat = lat;
                    if (lat > maxLat) maxLat = lat;
                }
                var result = new java.util.HashMap<String, Object>();
                result.put("type", FEATURE_COLLECTION);
                result.put(FEATURES, filtered);
                if (!filtered.isEmpty()) {
                    result.put("bbox", java.util.List.of(minLon, minLat, maxLon, maxLat));
                }
                result.put("count", filtered.size());
                return result;
            });
    }

    private List<Map<String, Object>> getFeatures(Map<String, Object> featureCollection) {
        Object obj = featureCollection.get(FEATURES);
        if (obj instanceof List<?> list) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map<?, ?> map) {
                    result.add(new HashMap<>((Map<String, Object>) map));
                }
            }
            return result;
        }
        return List.of();
    }

    private Map<String, Object> getProperties(Map<String, Object> feature) {
        Object obj = feature.get("properties");
        if (obj instanceof Map<?, ?> map) {
            return new HashMap<>((Map<String, Object>) map);
        }
        return Map.of();
    }

    private Map<String, Object> getCardData(Map<String, Object> props) {
        Object obj = props.get("cardData");
        if (obj instanceof Map<?, ?> map) {
            return new HashMap<>((Map<String, Object>) map);
        }
        return Map.of();
    }

    private Map<String, Object> getGeometry(Map<String, Object> feature) {
        Object obj = feature.get("geometry");
        if (obj instanceof Map<?, ?> map) {
            return new HashMap<>((Map<String, Object>) map);
        }
        return Map.of();
    }

    public Mono<Map<String, Object>> getPropertyById(String id) {
        Map<String, Object> all = mockFeatureCollection().block();
        if (all == null) return Mono.just(Collections.singletonMap(ERROR, "Property not found"));
        return getFeatures(all).stream()
                .filter(f -> idEquals(f, id))
                .findFirst()
                .map(f -> {
                    var result = new HashMap<>(f);
                    result.put("recommendedZoom", 15);
                    return (Map<String, Object>) result;
                })
                .map(Mono::just)
                .orElseGet(() -> Mono.just(Collections.singletonMap(ERROR, "Property not found")));
    }

    private boolean matchesFilter(Map<String, Object> feature, PropertySearchFilterDTO filter) {
        Map<String, Object> props = getProperties(feature);
        Map<String, Object> card = getCardData(props);
        var predicates = List.<Predicate<Map<String, Object>>>of(
            f -> Optional.ofNullable(filter.getMinPrice())
                    .map(min -> parsePrice(card.get("fullPrice")))
                    .map(price -> price.compareTo(filter.getMinPrice()) >= 0)
                    .orElse(true),
            f -> Optional.ofNullable(filter.getMaxPrice())
                    .map(max -> parsePrice(card.get("fullPrice")))
                    .map(price -> price.compareTo(filter.getMaxPrice()) <= 0)
                    .orElse(true),
            f -> Optional.ofNullable(filter.getMaxMonthlyPayment())
                    .map(max -> parsePrice(card.get("fullPrice")))
                    .map(price -> price.multiply(BigDecimal.valueOf(0.005)).compareTo(filter.getMaxMonthlyPayment()) <= 0)
                    .orElse(true),
            f -> Optional.ofNullable(filter.getBedrooms())
                    .map(bd -> parseInt(card.get("bedrooms")))
                    .map(bdVal -> bdVal >= filter.getBedrooms())
                    .orElse(true),
            f -> Optional.ofNullable(filter.getBathrooms())
                    .map(ba -> parseInt(card.get("bathrooms")))
                    .map(baVal -> baVal >= filter.getBathrooms())
                    .orElse(true),
            f -> Optional.ofNullable(filter.getMinLotSize())
                    .map(min -> parseInt(card.get("area")))
                    .map(areaVal -> areaVal >= filter.getMinLotSize())
                    .orElse(true),
            f -> Optional.ofNullable(filter.getMaxLotSize())
                    .map(max -> parseInt(card.get("area")))
                    .map(areaVal -> areaVal <= filter.getMaxLotSize())
                    .orElse(true)
        );
        return predicates.stream().allMatch(p -> p.test(feature));
    }

    private boolean addressContains(Map<String, Object> feature, String query) {
        Map<String, Object> props = getProperties(feature);
        Map<String, Object> card = getCardData(props);
        String address = Objects.toString(card.get("address"), "");
        return address.toLowerCase().contains(query.toLowerCase());
    }

    private boolean idEquals(Map<String, Object> feature, String id) {
        Map<String, Object> props = getProperties(feature);
        return Objects.equals(props.get("id"), id);
    }

    private Map<String, Object> featureCollection(List<Map<String, Object>> features) {
        return Map.of("type", FEATURE_COLLECTION, FEATURES, features);
    }

    private Map<String, Object> emptyFeatureCollection() {
        return Map.of("type", FEATURE_COLLECTION, FEATURES, List.of());
    }

    private Map<String, Object> feature(String id, String pinColor, String priceShort, String fullPrice, String address, String details, String imageUrl, boolean isFavorited, String badge, double lon, double lat) {
        Map<String, Object> cardData = new HashMap<>();
        String[] detailsArr = details.split("\\|");
        cardData.put("bedrooms", detailsArr.length > 0 ? detailsArr[0].trim() : "");
        cardData.put("bathrooms", detailsArr.length > 1 ? detailsArr[1].trim() : "");
        cardData.put("area", detailsArr.length > 2 ? detailsArr[2].trim() : "");
        cardData.put("address", address);
        cardData.put("isFavorited", isFavorited);
        cardData.put("imageUrl", imageUrl);
        cardData.put("fullPrice", fullPrice);
        if (badge != null) cardData.put("badge", badge);
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", id);
        properties.put("pinColor", pinColor);
        properties.put("priceShort", priceShort);
        properties.put("cardData", cardData);
        properties.put("cluster", false);
        Map<String, Object> geometry = new HashMap<>();
        geometry.put("type", "Point");
        geometry.put("coordinates", List.of(lon, lat));
        Map<String, Object> feature = new HashMap<>();
        feature.put("type", "Feature");
        feature.put("geometry", geometry);
        feature.put("properties", properties);
        return feature;
    }

    private int parseInt(Object value) {
        if (value == null) return 0;
        String str = value.toString().replaceAll("[^0-9]", "");
        if (str.isEmpty()) return 0;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private BigDecimal parsePrice(Object priceObj) {
        if (priceObj == null) return BigDecimal.ZERO;
        String str = priceObj.toString().replaceAll("[^0-9.]", "");
        if (str.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(str.replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}

