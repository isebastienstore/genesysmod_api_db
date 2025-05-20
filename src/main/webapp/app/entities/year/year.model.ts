export interface IYear {
  id: string;
  year?: number | null;
}

export type NewYear = Omit<IYear, 'id'> & { id: null };
