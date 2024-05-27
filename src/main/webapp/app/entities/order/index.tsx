import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Order from './order';
import OrderDetail from './order-detail';
import OrderUpdate from './order-update';
import OrderPayDialog from './order-pay-dialog';
import OrderCancelDialog from './order-cancel-dialog';

const OrderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Order />} />
    <Route path="new" element={<OrderUpdate />} />
    <Route path=":id">
      <Route index element={<OrderDetail />} />
      <Route path="pay" element={<OrderPayDialog />} />
      <Route path="cancel" element={<OrderCancelDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderRoutes;
