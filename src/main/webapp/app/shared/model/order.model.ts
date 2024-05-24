import dayjs from 'dayjs';

export interface IOrder {
  id?: number;
  created?: dayjs.Dayjs | null;
  paid?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOrder> = {};
