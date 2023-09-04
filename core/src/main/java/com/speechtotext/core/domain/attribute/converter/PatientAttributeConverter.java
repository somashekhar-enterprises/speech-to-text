package com.speechtotext.core.domain.attribute.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speechtotext.core.domain.attribute.PatientAttribute;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientAttributeConverter implements AttributeConverter<PatientAttribute, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientAttributeConverter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PatientAttribute attribute) {
        String attr = null;
        try {
            attr = MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to write patient attributes to database");
        }

        return attr;
    }

    @Override
    public PatientAttribute convertToEntityAttribute(String dbData) {
        PatientAttribute attribute = null;
        try {
            attribute =  MAPPER.readValue(dbData, PatientAttribute.class);
        } catch (Exception e) {
            LOGGER.error("Error reading value from database, possible corruption of values!");
        }
        return attribute;
    }
}
