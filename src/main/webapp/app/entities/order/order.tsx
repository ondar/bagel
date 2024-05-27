import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './order.reducer';

export const Order = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const orderList = useAppSelector(state => state.order.entities);
  const loading = useAppSelector(state => state.order.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="order-heading" data-cy="OrderHeading">
        Orders
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Order
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderList && orderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('created')}>
                  Created <FontAwesomeIcon icon={getSortIconByFieldName('created')} />
                </th>
                <th className="hand" onClick={sort('paid')}>
                  Paid <FontAwesomeIcon icon={getSortIconByFieldName('paid')} />
                </th>
                <th className="hand" onClick={sort('cancelled')}>
                  Cancelled <FontAwesomeIcon icon={getSortIconByFieldName('cancelled')} />
                </th>
                <th>Expires in</th>
                <th>Order lines</th>
                <th>Price</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderList.map((order, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order/${order.id}`} color="link" size="sm">
                      {order.id}
                    </Button>
                  </td>
                  <td>{order.created ? <TextFormat type="date" value={order.created} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{order.paid ? <TextFormat type="date" value={order.paid} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{order.cancelled ? <TextFormat type="date" value={order.cancelled} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{order.expiresIn}</td>
                  <td>{order.orderLines.length}</td>
                  <td>{order.total}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order/${order.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>

                      <Button
                        onClick={() => (window.location.href = `/order/${order.id}/cancel`)}
                        color="danger"
                        size="sm"
                        data-cy="entityCancelButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Cancel</span>
                      </Button>

                      <Button onClick={() => (window.location.href = `/order/${order.id}/pay`)} size="sm" data-cy="orderPayButton">
                        <FontAwesomeIcon icon="save" /> <span className="d-none d-md-inline">Pay</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Orders found</div>
        )}
      </div>
    </div>
  );
};

export default Order;
