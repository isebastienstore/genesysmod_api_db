export interface ICountry {
  id: string;
  code?: string | null;
  name?: string | null;
}

export type NewCountry = Omit<ICountry, 'id'> & { id: null };
