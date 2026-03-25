package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.model.CreateProductRestModel;

public interface ProductService {

    String createProductByAsync(CreateProductRestModel productRestModel) ;


    String createProductBySync(CreateProductRestModel productRestModel) throws Exception;
}
