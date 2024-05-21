package br.com.fiap.pedidos.service;

import br.com.fiap.pedidos.dto.GeocodingResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeolocationService {

    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public double[] getLatLong(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GEOCODING_API_URL + "?address=" + cep + "&key=YOUR_API_KEY";

        GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
        if (response != null && response.getResults().length > 0) {
            double lat = response.getResults()[0].getGeometry().getLocation().getLat();
            double lng = response.getResults()[0].getGeometry().getLocation().getLng();
            return new double[] { lat, lng };
        }

        return null;
    }

    public double calculateDistance(double[] coord1, double[] coord2) {
        double lat1 = coord1[0];
        double lng1 = coord1[1];
        double lat2 = coord2[0];
        double lng2 = coord2[1];

        // Calcular a distância entre as coordenadas
        // Fórmula de Haversine ou outra fórmula de cálculo de distância geográfica
        // Retornar a distância em km ou milhas
        // Implementação de exemplo simplificada:
        double earthRadius = 6371; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}

