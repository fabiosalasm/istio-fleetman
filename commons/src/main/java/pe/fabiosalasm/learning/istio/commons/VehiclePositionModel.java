package pe.fabiosalasm.learning.istio.commons;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.key.OffsetDateTimeKeyDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Created by fabio.salas (fabio.salas@globant.com) on 7/10/2020
 **/
@Data
@JsonDeserialize(builder = VehiclePositionModel.VehiclePositionModelBuilder.class)
@Builder(builderClassName = "VehiclePositionModelBuilder", toBuilder = true)
public class VehiclePositionModel implements Comparable<VehiclePositionModel> {
    private final String name;
    private final BigDecimal lat;
    private final BigDecimal lng;
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonDeserialize(keyUsing = OffsetDateTimeKeyDeserializer.class)
    private final OffsetDateTime timestamp;
    private final BigDecimal speed;

    @Override
    public int compareTo(VehiclePositionModel o) {
        return o.timestamp.compareTo(this.timestamp);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class VehiclePositionModelBuilder {

    }
}
