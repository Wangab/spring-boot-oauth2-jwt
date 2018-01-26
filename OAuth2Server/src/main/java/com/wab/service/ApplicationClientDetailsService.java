package com.wab.service;

import com.wab.model.dao.AppClientDetaileRepository;
import com.wab.model.entity.AppBaseClientDetaile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationClientDetailsService implements ClientDetailsService {
    private static Logger LOG = LoggerFactory.getLogger(ApplicationClientDetailsService.class);
    @Autowired
    private AppClientDetaileRepository appClientDetaileRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails clientDetails = appClientDetaileRepository.findOne(clientId);
        LOG.info(clientDetails.toString());
        return clientDetails;
    }

    public ClientDetails saveClientDetails(ClientDetails clientDetails) throws ClientRegistrationException{
        return appClientDetaileRepository.save((AppBaseClientDetaile)clientDetails);
    }
}
