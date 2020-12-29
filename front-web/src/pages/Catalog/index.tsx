import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from 'core/types/Product';
import { makeRequest } from 'core/utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoader from './components/Loaders/ProductCardLoader';
import './styles.scss'
import Pagination from 'core/components/Pagination';

const Catalog = () => {

    // 1 - when the component start, we need to load the list of products. 
    // 2- WHen the list is available, populate a state in the component and list de products dynamically.

    const [productsResponse, setProductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsLoading] = useState(false);
    const [activePage, setActivePage] = useState(0);


    console.log(productsResponse);

    useEffect(() => {

        const params = {
            page: activePage,
            linesPerPage: 5
        }

        //show loader before the request

        setIsLoading(true);

        makeRequest({ url: '/products', params: params })
            .then(response => setProductsResponse(response.data))
            .finally(() => {
                // end loader after the request
                setIsLoading(false);
            });


    }, [activePage]); // when we pass the second parameter as an empty list, we are saying that we will do something when the component starts

    return (
        <div className="catalog-container">
            <h1 className="catalog-title">Catálogo de Produtos</h1>
            <div className="catalog-products">

                {isLoading ? <ProductCardLoader /> : (
                    productsResponse?.content.map(product => (
                        <Link
                            to={`/products/${product.id}`}
                            key={product.id}>
                            <ProductCard product={product} />
                        </Link>
                    )))}
                { }

            </div>
            {
                productsResponse?.totalPages &&

                (
                    <Pagination
                        totalPages={productsResponse?.totalPages}
                        activePage={activePage}
                        onChange={page => setActivePage(page)}
                    />
                )
            }

        </div>
    );
}

export default Catalog;