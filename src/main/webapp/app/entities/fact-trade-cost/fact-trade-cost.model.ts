import { IFuel } from 'app/entities/fuel/fuel.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';

export interface IFactTradeCost {
  id: string;
  fixedCost?: number | null;
  variableCost?: number | null;
  fuel?: IFuel | null;
  metadata?: IMetadata | null;
}

export type NewFactTradeCost = Omit<IFactTradeCost, 'id'> & { id: null };
