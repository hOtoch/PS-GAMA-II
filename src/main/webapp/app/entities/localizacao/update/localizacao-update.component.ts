import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocalizacao } from '../localizacao.model';
import { LocalizacaoService } from '../service/localizacao.service';
import { LocalizacaoFormGroup, LocalizacaoFormService } from './localizacao-form.service';

@Component({
  selector: 'jhi-localizacao-update',
  templateUrl: './localizacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocalizacaoUpdateComponent implements OnInit {
  isSaving = false;
  localizacao: ILocalizacao | null = null;

  protected localizacaoService = inject(LocalizacaoService);
  protected localizacaoFormService = inject(LocalizacaoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocalizacaoFormGroup = this.localizacaoFormService.createLocalizacaoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localizacao }) => {
      this.localizacao = localizacao;
      if (localizacao) {
        this.updateForm(localizacao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localizacao = this.localizacaoFormService.getLocalizacao(this.editForm);
    if (localizacao.id !== null) {
      this.subscribeToSaveResponse(this.localizacaoService.update(localizacao));
    } else {
      this.subscribeToSaveResponse(this.localizacaoService.create(localizacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalizacao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(localizacao: ILocalizacao): void {
    this.localizacao = localizacao;
    this.localizacaoFormService.resetForm(this.editForm, localizacao);
  }
}
