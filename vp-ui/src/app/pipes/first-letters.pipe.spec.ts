import { FirstLettersPipe } from './first-letters.pipe';

describe('FirstLettersPipe', () => {
  it('create an instance', () => {
    const pipe = new FirstLettersPipe();
    expect(pipe).toBeTruthy();
  });
});
