import dayjs from 'dayjs';
import { IOrderLine } from 'app/shared/model/order-line.model';

export interface IOrder {
  id?: number;
  created?: dayjs.Dayjs | null;
  paid?: dayjs.Dayjs | null;
  cancelled?: dayjs.Dayjs | null;
  expiresIn?: string | null;
  orderLines?: IOrderLine[];
  total?: number;
}

export const defaultValue: Readonly<IOrder> = {};
