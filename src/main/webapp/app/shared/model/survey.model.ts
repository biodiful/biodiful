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
    numberOfMatchesPerPool?: number;
    numberOfMatchesPerPool2?: number;
    numberOfMatchesPerPool3?: number;
    matchesDescription?: any;
    matchesDescriptionPool2?: any;
    matchesDescriptionPool3?: any;
    open?: boolean;
    language?: Language;
    uniqueChallengers?: boolean;
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
        public numberOfMatchesPerPool?: number,
        public numberOfMatchesPerPool2?: number,
        public numberOfMatchesPerPool3?: number,
        public matchesDescription?: any,
        public matchesDescriptionPool2?: any,
        public matchesDescriptionPool3?: any,
        public open?: boolean,
        public language?: Language,
        public uniqueChallengers?: boolean
    ) {
        this.open = this.open || false;
        this.uniqueChallengers = this.uniqueChallengers || false;
    }
}
