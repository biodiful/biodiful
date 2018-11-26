import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BiodifulSurveyModule } from './survey/survey.module';
import { BiodifulAnswerModule } from './answer/answer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        BiodifulSurveyModule,
        BiodifulAnswerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BiodifulEntityModule {}
