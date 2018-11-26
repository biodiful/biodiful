import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BiodifulSharedModule } from 'app/shared';
import {
    AnswerComponent,
    AnswerDetailComponent,
    AnswerUpdateComponent,
    AnswerDeletePopupComponent,
    AnswerDeleteDialogComponent,
    answerRoute,
    answerPopupRoute
} from './';

const ENTITY_STATES = [...answerRoute, ...answerPopupRoute];

@NgModule({
    imports: [BiodifulSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AnswerComponent, AnswerDetailComponent, AnswerUpdateComponent, AnswerDeleteDialogComponent, AnswerDeletePopupComponent],
    entryComponents: [AnswerComponent, AnswerUpdateComponent, AnswerDeleteDialogComponent, AnswerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BiodifulAnswerModule {}
