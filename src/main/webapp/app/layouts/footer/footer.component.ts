import { Component, ViewEncapsulation, inject } from '@angular/core';
import { TranslateDirective } from 'app/shared/language';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  imports: [TranslateDirective, RouterModule],
  styleUrls: ['./footer.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export default class FooterComponent {
  public readonly router = inject(Router);
}
