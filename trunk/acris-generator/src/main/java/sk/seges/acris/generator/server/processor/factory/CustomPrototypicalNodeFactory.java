package sk.seges.acris.generator.server.processor.factory;

import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.tags.AppletTag;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.BlockquoteTag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.DefinitionList;
import org.htmlparser.tags.DefinitionListBullet;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.DoctypeTag;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.FrameSetTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.JspTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ObjectTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.ProcessingInstructionTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.tags.TitleTag;

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
