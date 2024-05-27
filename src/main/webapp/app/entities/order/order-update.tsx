import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Table } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IOrderLine } from 'app/shared/model/order-line.model';
import { IOrder } from 'app/shared/model/order.model';
import { getEntity, createEntity, reset } from './order.reducer';

export const OrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);

  const [orderLines, setOrderLines] = useState([]);

  const handleClose = () => {
    navigate('/order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveLine = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.quantity < 1) {
      toast.error('Please select quantity of 1 or more to order');
      return;
    }

    const product = products.find(it => it.id.toString() === values.product?.toString());
    if (!product) {
      toast.error('Please select product to order');
      return;
    }

    const newLine = {
      quantity: values.quantity,
      price: product.price,
      product: product,
    } as IOrderLine;

    setOrderLines([...orderLines.filter(line => line.product.id !== product.id), newLine]);
  };

  const defaultValues = () =>
    isNew
      ? {
          quantity: 1,
          product: 1,
        }
      : {
          ...orderEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bagelApp.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            Create new order
          </h2>
        </Col>
      </Row>

      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm defaultValues={defaultValues()} onSubmit={saveLine}>
            {!isNew ? <ValidatedField name="id" required readOnly id="order-line-id" label="ID" validate={{ required: true }} /> : null}

            <ValidatedField id="order-line-product" name="product" data-cy="product" label="Product" type="select">
              <option value="" key="0" />
              {products
                ? products.map(produce => (
                    <option value={produce.id} key={produce.id}>
                      {produce.id}: {produce.name}, price: {produce.price} USD, in stock: {produce.stock}
                    </option>
                  ))
                : null}
            </ValidatedField>

            <ValidatedField label="Quantity" id="order-line-quantity" name="quantity" data-cy="quantity" type="text" />

            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
              <FontAwesomeIcon icon="save" />
              &nbsp; Add order line or modify existing line with the selected product
            </Button>
          </ValidatedForm>
        </Col>
      </Row>

      <Row className="justify-content-center">
        <Col md="8">
          <Table responsive>
            <thead>
              <tr>
                <th>Product</th>
                <th>Quantity (pcs)</th>
                <th>Price (USD)</th>
                <th>Total (USD)</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderLines.map((orderLine, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{orderLine.product.name}</td>
                  <td>{orderLine.quantity}</td>
                  <td>{orderLine.price}</td>
                  <td>{orderLine.price * orderLine.quantity}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>

      <Row className="justify-content-center">
        <Col md="8">
          <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />
            &nbsp;
            <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button onClick={() => dispatch(createEntity({ orderLines: orderLines }))} color="danger" data-cy="entityCancelButton">
            <FontAwesomeIcon icon="save" /> <span className="d-none d-md-inline">Submit order</span>
          </Button>
        </Col>
      </Row>
    </div>
  );
};

export default OrderUpdate;
