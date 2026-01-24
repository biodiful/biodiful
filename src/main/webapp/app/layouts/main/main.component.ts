import { Component, OnInit, Renderer2, RendererFactory2, Type, inject } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { filter } from 'rxjs/operators';
import { SurveyAnswerComponent } from 'app/survey/survey-answer/survey-answer.component';
import { SurveyPresentationComponent } from 'app/survey/survey-presentation/survey-presentation.component';
import HomeComponent from 'app/home/home.component1';
import { SurveyComponent } from 'app/entities/survey/list/survey.component';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, PageRibbonComponent],
})
export default class MainComponent implements OnInit {
  containerClass = 'container-fluid';

  private readonly renderer: Renderer2;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });

    // Update container class based on the current route component
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      const currentRoute = this.router.routerState.root;
      const component = this.traverseRouterState(currentRoute);

      if (component) {
        if (component === HomeComponent) {
          this.containerClass = 'container-fluid home';
        } else if (component === SurveyPresentationComponent) {
          this.containerClass = 'container-fluid survey-presentation';
        } else if (component === SurveyAnswerComponent) {
          this.containerClass = 'container-fluid survey-answer';
        } else if (component === SurveyComponent) {
          this.containerClass = 'container-fluid survey-list-component';
        } else {
          this.containerClass = 'container-fluid';
        }
      }
    });
  }

  // Helper method to traverse the router state tree
  private traverseRouterState(route: ActivatedRoute): Type<any> | null {
    // Get the component from the route snapshot
    const component = route.component;

    // Check child routes recursively
    if (route.firstChild) {
      return this.traverseRouterState(route.firstChild);
    }
    return component;
  }
}
