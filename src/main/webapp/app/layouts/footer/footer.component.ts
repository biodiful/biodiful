import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['footer.css'],
    encapsulation: ViewEncapsulation.None
})
export class FooterComponent {
    constructor(public router: Router) {}
}
