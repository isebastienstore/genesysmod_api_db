export interface IFuel {
  id: string;
  name?: string | null;
}

export type NewFuel = Omit<IFuel, 'id'> & { id: null };
