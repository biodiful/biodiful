import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { JhiLanguageHelper } from 'app/core';
import { SurveyAnswerComponent } from 'app/survey/survey-answer/survey-answer.component';
import { SurveyPresentationComponent } from 'app/survey/survey-presentation/survey-presentation.component';
import { HomeComponent } from 'app/home';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
    containerClass: String;

    constructor(private jhiLanguageHelper: JhiLanguageHelper, private router: Router) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'biodifulApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }

        // Update container class to target current component
        if (routeSnapshot.routeConfig && routeSnapshot.routeConfig.component) {
            if (routeSnapshot.routeConfig.component == HomeComponent) {
                this.containerClass = 'container-fluid home';
            } else if (routeSnapshot.routeConfig.component == SurveyPresentationComponent) {
                this.containerClass = 'container-fluid survey-presentation';
            } else if (routeSnapshot.routeConfig.component == SurveyAnswerComponent) {
                this.containerClass = 'container-fluid survey-answer';
            } else {
                this.containerClass = 'container-fluid';
            }
        }

        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
    }
}
