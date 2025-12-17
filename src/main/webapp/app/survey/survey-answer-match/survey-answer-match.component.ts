import { Component, computed, effect, input, output, signal } from '@angular/core';
import { Challenger } from 'app/shared/model/challenger.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-survey-answer-match',
  templateUrl: './survey-answer-match.component.html',
  standalone: true,
  styleUrl: './survey-answer-match.scss',
  imports: [SharedModule],
})
export class SurveyAnswerMatchComponent {
  // Show loader while retrieving media from S3
  challengerOne = input<Challenger | undefined, Challenger | undefined>(undefined, {
    transform: value => value ?? new Challenger('UNDEFINED_1', '/content/images/loader.gif', 'IMAGE'),
  });

  challengerTwo = input<Challenger | undefined, Challenger | undefined>(undefined, {
    transform: value => value ?? new Challenger('UNDEFINED_2', '/content/images/loader.gif', 'IMAGE'),
  });

  winnerSelected = output<Challenger>();

  // Media playback tracking
  leftPlaybackProgress = signal(0);
  rightPlaybackProgress = signal(0);
  leftPlayed90Percent = signal(false);
  rightPlayed90Percent = signal(false);
  randomPlayOrder = signal<'LEFT' | 'RIGHT'>('LEFT');

  // Computed: detect media type from first challenger
  mediaType = computed(() => this.challengerOne()?.type ?? 'IMAGE');

  // Computed: can submit when both have played 90% (or always for images)
  canSubmit = computed(() => {
    if (this.mediaType() === 'IMAGE') {
      return true; // Images can always be clicked
    }
    return this.leftPlayed90Percent() && this.rightPlayed90Percent();
  });

  constructor() {
    // Reset playback state whenever challengers change (new match)
    effect(() => {
      // Access the input signals to trigger the effect when they change
      const left = this.challengerOne();
      const right = this.challengerTwo();

      // Reset playback state for new match
      this.leftPlaybackProgress.set(0);
      this.rightPlaybackProgress.set(0);
      this.leftPlayed90Percent.set(false);
      this.rightPlayed90Percent.set(false);

      // Randomly decide which media plays first for this match
      this.randomPlayOrder.set(Math.random() < 0.5 ? 'LEFT' : 'RIGHT');
    });
  }

  // For images (existing behavior - direct click)
  challengerClicked(challenger: Challenger): void {
    if (this.mediaType() === 'IMAGE') {
      this.winnerSelected.emit(challenger);
    }
  }

  // For videos/audio - button click
  selectLeft(): void {
    if (this.canSubmit()) {
      this.winnerSelected.emit(this.challengerOne()!);
    }
  }

  selectRight(): void {
    if (this.canSubmit()) {
      this.winnerSelected.emit(this.challengerTwo()!);
    }
  }

  onLeftTimeUpdate(event: Event): void {
    const media = event.target as HTMLVideoElement | HTMLAudioElement;
    if (!media.duration) {
      return;
    }

    const progress = (media.currentTime / media.duration) * 100;
    this.leftPlaybackProgress.set(progress);

    if (progress >= 90) {
      this.leftPlayed90Percent.set(true);
    }
  }

  onRightTimeUpdate(event: Event): void {
    const media = event.target as HTMLVideoElement | HTMLAudioElement;
    if (!media.duration) {
      return;
    }

    const progress = (media.currentTime / media.duration) * 100;
    this.rightPlaybackProgress.set(progress);

    if (progress >= 90) {
      this.rightPlayed90Percent.set(true);
    }
  }
}
