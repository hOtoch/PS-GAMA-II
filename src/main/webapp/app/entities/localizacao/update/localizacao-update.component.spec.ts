import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { LocalizacaoService } from '../service/localizacao.service';
import { ILocalizacao } from '../localizacao.model';
import { LocalizacaoFormService } from './localizacao-form.service';

import { LocalizacaoUpdateComponent } from './localizacao-update.component';

describe('Localizacao Management Update Component', () => {
  let comp: LocalizacaoUpdateComponent;
  let fixture: ComponentFixture<LocalizacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localizacaoFormService: LocalizacaoFormService;
  let localizacaoService: LocalizacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LocalizacaoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LocalizacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocalizacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localizacaoFormService = TestBed.inject(LocalizacaoFormService);
    localizacaoService = TestBed.inject(LocalizacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const localizacao: ILocalizacao = { id: 21366 };

      activatedRoute.data = of({ localizacao });
      comp.ngOnInit();

      expect(comp.localizacao).toEqual(localizacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalizacao>>();
      const localizacao = { id: 19197 };
      jest.spyOn(localizacaoFormService, 'getLocalizacao').mockReturnValue(localizacao);
      jest.spyOn(localizacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localizacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localizacao }));
      saveSubject.complete();

      // THEN
      expect(localizacaoFormService.getLocalizacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(localizacaoService.update).toHaveBeenCalledWith(expect.objectContaining(localizacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalizacao>>();
      const localizacao = { id: 19197 };
      jest.spyOn(localizacaoFormService, 'getLocalizacao').mockReturnValue({ id: null });
      jest.spyOn(localizacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localizacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localizacao }));
      saveSubject.complete();

      // THEN
      expect(localizacaoFormService.getLocalizacao).toHaveBeenCalled();
      expect(localizacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalizacao>>();
      const localizacao = { id: 19197 };
      jest.spyOn(localizacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localizacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localizacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
