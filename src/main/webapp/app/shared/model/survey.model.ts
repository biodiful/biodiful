export interface ISurvey {
    id?: number;
    surveyName?: string;
    surveyDescription?: any;
    contactsDescription?: any;
    friendlyURL?: string;
    photoURL?: string;
    logosURL?: string;
    formURL?: string;
    challengersURL?: string;
    numberOfMatches?: number;
    matchesDescription?: any;
    open?: boolean;
}

export class Survey implements ISurvey {
    constructor(
        public id?: number,
        public surveyName?: string,
        public surveyDescription?: any,
        public contactsDescription?: any,
        public friendlyURL?: string,
        public photoURL?: string,
        public logosURL?: string,
        public formURL?: string,
        public challengersURL?: string,
        public numberOfMatches?: number,
        public matchesDescription?: any,
        public open?: boolean
    ) {
        this.open = this.open || false;
    }
}
