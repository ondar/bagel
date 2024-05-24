import product from 'app/entities/product/product.reducer';
import order from 'app/entities/order/order.reducer';
import orderLine from 'app/entities/order-line/order-line.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  order,
  orderLine,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
