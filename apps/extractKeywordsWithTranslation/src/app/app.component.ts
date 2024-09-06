import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Subject, mergeMap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  standalone: true,
  imports: [],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'extractKeywordsWithTranslation';

  private readonly storePictures$ = new Subject<File[]>();
  private readonly httpClient = inject(HttpClient);

  constructor() {
    this.storePictures$
      .pipe(
        mergeMap((files) => {
          console.log('storePictures files', files);

          const formData = new FormData();
          files.forEach((file) => {
            formData.append('text', file, file.name);
          });
          return this.httpClient.post(
            ' http://localhost:8080/translate/analyze',
            formData
          );
        }),
        takeUntilDestroyed()
      )
      .subscribe((files) => {});
  }

  onAddText(target: EventTarget | null) {
    if (!target) {
      return;
    }

    const files = (target as HTMLInputElement).files;

    if (!files) {
      return;
    }
    const filesToUpload: File[] = [];
    for (let i = 0; i < files.length; i++) {
      const file = files.item(i);
      if (!file) {
        continue;
      }
      filesToUpload.push(file);
    }

    this.storePictures$.next(filesToUpload);
  }
}
