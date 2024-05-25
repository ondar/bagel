import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderLine from './order-line';
import OrderLineDetail from './order-line-detail';
import OrderLineUpdate from './order-line-update';
import OrderLineDeleteDialog from './order-line-delete-dialog';

const OrderLineRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderLine />} />
    <Route path="new" element={<OrderLineUpdate />} />
    <Route path=":id">
      <Route index element={<OrderLineDetail />} />
      <Route path="edit" element={<OrderLineUpdate />} />
      <Route path="delete" element={<OrderLineDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderLineRoutes;
