package sk.seges.acris.generator.server.processor.post;

/**
 * Created by PeterSimun on 5.10.2014.
 */
public enum TokenSupport {
    NONE {
        @Override
        public boolean contains(TokenSupport tokenSupport) {
            return false;
        }
    }, ALL {
        @Override
        public boolean contains(TokenSupport tokenSupport) {
            return true;
        }
    }, DEFAULT_ONLY, NON_DEFAULT;

    public boolean contains(TokenSupport tokenSupport) {
        return this.equals(tokenSupport);
    }
}