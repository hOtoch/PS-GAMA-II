import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILocalizacao } from '../localizacao.model';
import { LocalizacaoService } from '../service/localizacao.service';

@Component({
  templateUrl: './localizacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LocalizacaoDeleteDialogComponent {
  localizacao?: ILocalizacao;

  protected localizacaoService = inject(LocalizacaoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localizacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
