import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BiodifulSharedModule } from 'app/shared';
import { ABOUT_ROUTE, AboutComponent } from './';

@NgModule({
    imports: [BiodifulSharedModule, RouterModule.forChild([ABOUT_ROUTE])],
    declarations: [AboutComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BiodifulAboutModule {}
