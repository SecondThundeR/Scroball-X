package su.secondthunder.scroball.transforms;

import su.secondthunder.scroball.Track;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class TitleExtractorTest {

  private TitleExtractor titleExtractor = new TitleExtractor();

  @Test
  public void transform_doesNotModifyWhenNoSeparator() {
    Track input = Track.builder().track("Some Title").artist("").build();

    Track output = titleExtractor.transform(input);

    assertThat(output.track()).isEqualTo("Some Title");
  }
}
