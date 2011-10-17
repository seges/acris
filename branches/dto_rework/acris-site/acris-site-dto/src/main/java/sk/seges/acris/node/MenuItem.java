package sk.seges.acris.node;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

import com.google.gwt.user.client.History;

@BeanWrapper
public class MenuItem implements Serializable {

	private static final long serialVersionUID = 8025791600224090517L;

	/**
	 * label
	 */
	private String l;

	/**
	 * language
	 */
	private String g;

	/**
	 * niceUrl
	 */
	private String n;

	/**
	 * permission
	 */
	private String p;
	
	/**
	 * children
	 */
	private List<MenuItem> c;

	/**
	 * visibility
	 */
	private boolean visible = true;

	/**
	 * label
	 */
	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	/**
	 * language
	 */
	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	/**
	 * niceUrl
	 */
	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    /**
	 * children
	 */
	public List<MenuItem> getC() {
		return c;
	}

	public void setC(List<MenuItem> c) {
		this.c = c;
	}

	/**
	 * visibility
	 */
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void onActivation() {
		History.newItem(n);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((l == null) ? 0 : l.hashCode());
        result = prime * result + ((n == null) ? 0 : n.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuItem other = (MenuItem) obj;
        if (l == null) {
            if (other.l != null)
                return false;
        } else if (!l.equals(other.l))
            return false;
        if (n == null) {
            if (other.n != null)
                return false;
        } else if (!n.equals(other.n))
            return false;
        return true;
    }
}
