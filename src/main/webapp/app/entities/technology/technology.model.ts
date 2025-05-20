import { CategoryType } from 'app/entities/enumerations/category-type.model';

export interface ITechnology {
  id: string;
  name?: string | null;
  category?: keyof typeof CategoryType | null;
}

export type NewTechnology = Omit<ITechnology, 'id'> & { id: null };
