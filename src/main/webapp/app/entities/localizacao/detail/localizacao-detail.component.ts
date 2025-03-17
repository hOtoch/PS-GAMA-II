import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILocalizacao } from '../localizacao.model';

@Component({
  selector: 'jhi-localizacao-detail',
  templateUrl: './localizacao-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LocalizacaoDetailComponent {
  localizacao = input<ILocalizacao | null>(null);

  previousState(): void {
    window.history.back();
  }
}
