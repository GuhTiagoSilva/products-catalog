import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from '../../core/types/Product';
import { makeRequest } from '../../core/utils/request';
import ProductCard from './components/ProductCard';
import './styles.scss'

const Catalog = () => {

    // 1 - when the component start, we need to load the list of products. 
    // 2- WHen the list is available, populate a state in the component and list de products dynamically.

    const [productsResponse, setProductsResponse] = useState<ProductsResponse>();

    console.log(productsResponse);

    useEffect(() => {

        const params = {
            page: 0,
            linesPerPage: 20
        }

        makeRequest({ url: '/products', params: params })
            .then(response => setProductsResponse(response.data));

    }, []); // when we pass the second parameter as an empty list, we are saying that we will do something when the component starts

    return (
        <div className="catalog-container">
            <h1 className="catalog-title">Catálogo de Produtos</h1>
            <div className="catalog-products">
                {productsResponse?.content.map(product => (
                    <Link to={`/products/${product.id}`} key={product.id}><ProductCard product={product} /></Link>
                ))}
            </div>
        </div>
    );
}

export default Catalog;