import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../localizacao.test-samples';

import { LocalizacaoFormService } from './localizacao-form.service';

describe('Localizacao Form Service', () => {
  let service: LocalizacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocalizacaoFormService);
  });

  describe('Service methods', () => {
    describe('createLocalizacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocalizacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            endereco: expect.any(Object),
            cidade: expect.any(Object),
            estado: expect.any(Object),
            cep: expect.any(Object),
          }),
        );
      });

      it('passing ILocalizacao should create a new form with FormGroup', () => {
        const formGroup = service.createLocalizacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            endereco: expect.any(Object),
            cidade: expect.any(Object),
            estado: expect.any(Object),
            cep: expect.any(Object),
          }),
        );
      });
    });

    describe('getLocalizacao', () => {
      it('should return NewLocalizacao for default Localizacao initial value', () => {
        const formGroup = service.createLocalizacaoFormGroup(sampleWithNewData);

        const localizacao = service.getLocalizacao(formGroup) as any;

        expect(localizacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocalizacao for empty Localizacao initial value', () => {
        const formGroup = service.createLocalizacaoFormGroup();

        const localizacao = service.getLocalizacao(formGroup) as any;

        expect(localizacao).toMatchObject({});
      });

      it('should return ILocalizacao', () => {
        const formGroup = service.createLocalizacaoFormGroup(sampleWithRequiredData);

        const localizacao = service.getLocalizacao(formGroup) as any;

        expect(localizacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocalizacao should not enable id FormControl', () => {
        const formGroup = service.createLocalizacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocalizacao should disable id FormControl', () => {
        const formGroup = service.createLocalizacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
