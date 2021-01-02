import React from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';
const Form = () => {
    return (
        <BaseForm title="CADASTRAR UM PRODUTO">
            <div className="row">
                <div className="col-6">
                    <input className="form-control"></input>
                </div>
            </div>
        </BaseForm>
    );
}

export default Form;