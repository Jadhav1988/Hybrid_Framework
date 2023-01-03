package com.org.resourceUtils;

/**
 * PAUSE_LENGTH
 *
 * @version $Revision: 1.16 $
 */
public enum PAUSE_LENGTH
{
  //~ Enum constants -------------------------------------------------------------------------------

  MAX(60), MIN(5), AJAX(10), AVG(30);

  //~ Instance Variables ---------------------------------------------------------------------------

  private Integer value;

  //~ Constructors ---------------------------------------------------------------------------------

  /**
   * Creates a new PAUSE_LENGTH object.
   *
   * @param value DOCUMENT ME!
   */
  private PAUSE_LENGTH(Integer value)
  {
    this.value = value;
  }

  //~ Methods --------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Integer value()
  {
    return this.value;
  }
}
