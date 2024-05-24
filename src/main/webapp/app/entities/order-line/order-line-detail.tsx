import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-line.reducer';

export const OrderLineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderLineEntity = useAppSelector(state => state.orderLine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderLineDetailsHeading">Order Line</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{orderLineEntity.id}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{orderLineEntity.quantity}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{orderLineEntity.price}</dd>
          <dt>Product</dt>
          <dd>{orderLineEntity.product ? orderLineEntity.product.id : ''}</dd>
          <dt>Order</dt>
          <dd>{orderLineEntity.order ? orderLineEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-line" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-line/${orderLineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderLineDetail;
