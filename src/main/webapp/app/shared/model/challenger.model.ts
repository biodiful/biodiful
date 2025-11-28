export type MediaType = 'IMAGE' | 'VIDEO' | 'AUDIO';

export interface IChallenger {
  id: string;
  url: string;
  type?: MediaType;
}

export class Challenger implements IChallenger {
  constructor(
    public id: string,
    public url: string,
    public type?: MediaType,
  ) {}
}
