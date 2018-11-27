export interface IChallenger {
    id: string;
    url: string;
}

export class Challenger implements IChallenger {
    constructor(public id: string, public url: string) {}
}
