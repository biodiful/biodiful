import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'jhi-about',
  templateUrl: './about.component.html',
  styleUrl: './about.scss',
  standalone: true,
  imports: [CommonModule, TranslateDirective, RouterModule],
})
export default class AboutComponent {}
