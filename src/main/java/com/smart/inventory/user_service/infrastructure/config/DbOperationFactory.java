package com.smart.inventory.user_service.infrastructure.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.demo.dboperation.Dboperation;

@Component
@Scope("singleton")
public class DbOperationFactory {
	
    private final DataSourcePropertiesCustom props;

    public DbOperationFactory(DataSourcePropertiesCustom props) {
        this.props = props;
    }

    public Dboperation createDbOperation() {
        return new Dboperation(props.getUrl(), props.getUsername(), props.getPassword());
    }


}
