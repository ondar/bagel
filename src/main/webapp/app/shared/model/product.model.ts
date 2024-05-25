export interface IProduct {
  id?: number;
  name?: string | null;
  stock?: number | null;
  price?: number | null;
}

export const defaultValue: Readonly<IProduct> = {};
