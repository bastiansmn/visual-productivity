package com.bastiansmn.vp.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ProjectIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        Connection connection = sharedSessionContractImplementor.connection();
        try {
            // Generate a random string of 8 characters
            String randomString = RandomStringUtils.randomAlphanumeric(8);

            // Check if the generated string is already in use
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT project_id FROM projects WHERE project_id = '" + randomString + "'"
            );

            while (resultSet.next()) randomString = RandomStringUtils.randomAlphanumeric(8);

            return randomString;

        } catch (SQLException e) {
            log.error("Error while generating project id", e);
        }

        return null;
    }
}
