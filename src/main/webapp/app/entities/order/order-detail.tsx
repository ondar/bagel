import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">Order</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{orderEntity.id}</dd>

          <dt>
            <span id="created">Created</span>
          </dt>
          <dd>{orderEntity.created ? <TextFormat value={orderEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>

          <dt>
            <span id="paid">Paid</span>
          </dt>
          <dd>{orderEntity.paid ? <TextFormat value={orderEntity.paid} type="date" format={APP_DATE_FORMAT} /> : 'never'}</dd>

          <dt>
            <span id="cancelled">Cancelled</span>
          </dt>
          <dd>{orderEntity.cancelled ? <TextFormat value={orderEntity.cancelled} type="date" format={APP_DATE_FORMAT} /> : 'never'}</dd>

          <dt>
            <span id="expiresIn">Expires in</span>
          </dt>
          <dd>{orderEntity.expiresIn}</dd>

          <dt>
            <span id="orderLines">Order lines</span>
          </dt>
          <dd>
            <div className="table-responsive">
              {orderEntity.orderLines && orderEntity.orderLines.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Product</th>
                      <th>Quantity (pcs)</th>
                      <th>Price (USD)</th>
                      <th>Total (USD)</th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {orderEntity.orderLines.map((orderLine, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>{orderLine.id}</td>
                        <td>{orderLine.product.name}</td>
                        <td>{orderLine.quantity}</td>
                        <td>{orderLine.price}</td>
                        <td>{orderLine.price * orderLine.quantity}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <div className="alert alert-warning">No products found</div>
              )}
            </div>
          </dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
