import { makeRequest } from "core/utils/request";
import React, { useState } from "react";
import BaseForm from "../../BaseForm";
import "./styles.scss";

type FormState = {
  name: string;
  price: string;
  category: string;
};

const Form = () => {
  const [formData, setFormData] = useState<FormState>({
    name: "",
    price: "",
    category: "",
  });

  const handleOnChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const name = event.target.name;
    const value = event.target.value;

    setFormData((data) => ({ ...data, [name]: value }));
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <BaseForm title="CADASTRAR UM PRODUTO">
        <div className="row">
          <div className="col-6">
            <input
              className="form-control mb-5 mt-5"
              name="name"
              value={formData.name}
              onChange={handleOnChange}
              placeholder="Nome do Produto"
            ></input>
            <select
              className="form-control mb-5"
              name="category"
              value={formData.category}
              onChange={handleOnChange}
            >
              <option value="livros">Livros</option>
              <option value="computadores">Computadores</option>
              <option value="eletronicos">Eletrônicos</option>
            </select>
            <input
              className="form-control"
              name="price"
              value={formData.price}
              onChange={handleOnChange}
              placeholder="Preço"
            ></input>
          </div>
        </div>
      </BaseForm>
    </form>
  );
};

export default Form;
