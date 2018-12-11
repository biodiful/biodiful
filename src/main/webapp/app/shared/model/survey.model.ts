export const enum Language {
    FR = 'FR',
    EN = 'EN',
    ES = 'ES'
}

export interface ISurvey {
    id?: number;
    surveyName?: string;
    surveyDescription?: any;
    contactsDescription?: any;
    friendlyURL?: string;
    photoURL?: string;
    logosURL?: string;
    formURL?: string;
    challengersPool1URL?: string;
    challengersPool2URL?: string;
    challengersPool3URL?: string;
    numberOfMatches?: number;
    matchesDescription?: any;
    open?: boolean;
    language?: Language;
    withRemise?: boolean;
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
        public challengersPool1URL?: string,
        public challengersPool2URL?: string,
        public challengersPool3URL?: string,
        public numberOfMatches?: number,
        public matchesDescription?: any,
        public open?: boolean,
        public language?: Language,
        public withRemise?: boolean
    ) {
        this.open = this.open || false;
        this.withRemise = this.withRemise || false;
    }
}
