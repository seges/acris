package sk.seges.acris.generator.server.processor.factory;

import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.tags.*;
import sk.seges.acris.generator.server.processor.htmltags.LenientLinkTag;
import sk.seges.acris.generator.server.processor.htmltags.NoScriptTag;
import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;

public class CustomPrototypicalNodeFactory extends PrototypicalNodeFactory {

	private static final long serialVersionUID = 2828409970071638754L;

	@Override
	public PrototypicalNodeFactory registerTags() {
		registerTag(new AppletTag());
		registerTag(new BaseHrefTag());
		registerTag(new Bullet());
		registerTag(new BulletList());
		registerTag(new DefinitionList());
		registerTag(new DefinitionListBullet());
		registerTag(new DoctypeTag());
		registerTag(new FormTag());
		registerTag(new FrameSetTag());
		registerTag(new FrameTag());
		registerTag(new HeadingTag());
		registerTag(new ImageTag());
		registerTag(new InputTag());
		registerTag(new JspTag());
		registerTag(new LabelTag());
		registerTag(new MetaTag());
		registerTag(new ObjectTag());
		registerTag(new OptionTag());
		registerTag(new ParagraphTag());
		registerTag(new ProcessingInstructionTag());
		registerTag(new ScriptTag());
		registerTag(new SelectTag());
		registerTag(new StyleTag());
		registerTag(new TableColumn());
		registerTag(new TableHeader());
		registerTag(new TableRow());
		registerTag(new TableTag());
		registerTag(new TextareaTag());
		registerTag(new TitleTag());
		registerTag(new Div());
		registerTag(new BlockquoteTag());
		registerTag(new Span());
		registerTag(new BodyTag());
		registerTag(new HeadTag());
		registerTag(new Html());

		registerTag(new StyleLinkTag());
		registerTag(new NoScriptTag());
		registerTag(new LenientLinkTag());

		return this;
	}
}
