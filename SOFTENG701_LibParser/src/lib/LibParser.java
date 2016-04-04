/* Generated By:JavaCC: Do not edit this line. LibParser.java */
package lib;

public class LibParser implements LibParserConstants {
  public static void ExactlyOneAssert(int count, String subEntry, String parentEntry)
  throws ParseException
  {
    if(count != 1)
    {
      throw new ParseException("There should be only one "
      + subEntry
      + " entry in "
      + parentEntry
      + "!");
    }
  }

  public static void MoreThanOneAssert(int count, String subEntry, String parentEntry)
  throws ParseException
  {
    if(count < 1)
    {
      throw new ParseException("There should be more than one "
      + subEntry
      + " entry in "
      + parentEntry
      + "!");
    }
  }

  final public void Library() throws ParseException {
    LibraryItem();
    jj_consume_token(0);
  }

  final public void LibraryItem() throws ParseException {
  int websiteCount = 0,
  buildingCount = 0,
  timeofyearCount = 0,
  staffCount = 0,
  bookcollectionCount = 0;
    jj_consume_token(LIBRARY);
    jj_consume_token(LABEL);
    jj_consume_token(LBRACE);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WEBSITE:
        WebsiteItem();
                   websiteCount++;
        break;
      case BUILDING:
        BuildingItem();
                      buildingCount++;
        break;
      case TIME_OF_YEAR:
        TimeOfYearItem();
                        timeofyearCount++;
        break;
      case STAFF:
        StaffItem();
                   staffCount++;
        break;
      case BOOK_COLLECTION:
        BookCollectionItem();
                            bookcollectionCount++;
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WEBSITE:
      case BUILDING:
      case TIME_OF_YEAR:
      case STAFF:
      case BOOK_COLLECTION:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
    LibParser.ExactlyOneAssert(websiteCount, "WEBSITE", "LIBRARY");
    LibParser.ExactlyOneAssert(buildingCount, "BUILDING", "LIBRARY");
    LibParser.MoreThanOneAssert(timeofyearCount, "TIMEOFYEAR", "LIBRARY");
    LibParser.MoreThanOneAssert(staffCount, "STAFF", "LIBRARY");
    LibParser.MoreThanOneAssert(bookcollectionCount, "BOOKCOLLECTION", "LIBRARY");
    jj_consume_token(RBRACE);
  }

  final public void WebsiteItem() throws ParseException {
    jj_consume_token(WEBSITE);
    jj_consume_token(WEBSITE_ADDRESS);
  }

  final public void Date() throws ParseException {
    jj_consume_token(WEEK);
    jj_consume_token(COMMA);
    jj_consume_token(DAY);
    jj_consume_token(MONTH);
    jj_consume_token(YEAR);
  }

  final public void Hours() throws ParseException {
    jj_consume_token(WEEK_SHORT);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMMA);
      jj_consume_token(WEEK_SHORT);
    }
    jj_consume_token(LPAREN);
    Period();
    jj_consume_token(RPAREN);
  }

  final public void Period() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TIME:
      Time();
      jj_consume_token(TO);
      Time();
      break;
    case CLOSED:
      jj_consume_token(CLOSED);
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Time() throws ParseException {
    jj_consume_token(TIME);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 68:
      jj_consume_token(68);
      break;
    case 69:
      jj_consume_token(69);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void BuildingItem() throws ParseException {
    jj_consume_token(BUILDING);
    jj_consume_token(BUILDING_NUMBER);
  }

  final public void TimeOfYearItem() throws ParseException {
    jj_consume_token(TIME_OF_YEAR);
    jj_consume_token(LABEL);
    jj_consume_token(LBRACE);
    StartItem();
    EndItem();
    label_3:
    while (true) {
      HoursItem();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HOURS:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
    }
    jj_consume_token(RBRACE);
  }

  final public void StartItem() throws ParseException {
    jj_consume_token(START);
    Date();
  }

  final public void EndItem() throws ParseException {
    jj_consume_token(END);
    Date();
  }

  final public void HoursItem() throws ParseException {
    jj_consume_token(HOURS);
    Hours();
  }

  final public void StaffItem() throws ParseException {
  int positionCount = 0, emailCount = 0, typeCount = 0, rateCount = 0;
    jj_consume_token(STAFF);
    jj_consume_token(LABEL);
    jj_consume_token(LBRACE);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case POSITION:
        PositionItem();
                    positionCount++;
        break;
      case EMAIL:
        EmailItem();
                   emailCount++;
        break;
      case TYPE:
        TypeItem();
                  typeCount++;
        break;
      case RATE:
        RateItem();
                  rateCount++;
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case POSITION:
      case EMAIL:
      case TYPE:
      case RATE:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
    }
    LibParser.ExactlyOneAssert(positionCount, "BUILDING", "STAFF");
    LibParser.MoreThanOneAssert(emailCount, "EMAIL", "STAFF");
    LibParser.ExactlyOneAssert(typeCount, "TYPE", "STAFF");
    LibParser.ExactlyOneAssert(rateCount, "RATE", "STAFF");
    jj_consume_token(RBRACE);
  }

  final public void PositionItem() throws ParseException {
    jj_consume_token(POSITION);
    jj_consume_token(LABEL);
  }

  final public void EmailItem() throws ParseException {
    jj_consume_token(EMAIL);
    jj_consume_token(EMAIL_ADDRESS);
  }

  final public void TypeItem() throws ParseException {
    jj_consume_token(TYPE);
    jj_consume_token(TYPE_OPTION);
  }

  final public void RateItem() throws ParseException {
    jj_consume_token(RATE);
    jj_consume_token(RATE_VALUE);
  }

  final public void BookCollectionItem() throws ParseException {
    jj_consume_token(BOOK_COLLECTION);
    jj_consume_token(LABEL);
    jj_consume_token(LBRACE);
    label_5:
    while (true) {
      BookItem();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOK:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_5;
      }
    }
    jj_consume_token(RBRACE);
  }

  final public void BookItem() throws ParseException {
  int titleCount = 0, authorCount = 0;
    jj_consume_token(BOOK);
    jj_consume_token(BOOK_ID);
    jj_consume_token(LBRACE);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TITLE:
        TitleItem();
                 titleCount++;
        break;
      case AUTHOR:
        AuthorItem();
                    authorCount++;
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TITLE:
      case AUTHOR:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_6;
      }
    }
    LibParser.ExactlyOneAssert(titleCount, "TITLE", "BOOK");
    LibParser.MoreThanOneAssert(authorCount, "AUTHOR", "BOOK");
    jj_consume_token(RBRACE);
  }

  final public void TitleItem() throws ParseException {
    jj_consume_token(TITLE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOK_TITLE:
      jj_consume_token(BOOK_TITLE);
      break;
    case LABEL:
      jj_consume_token(LABEL);
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void AuthorItem() throws ParseException {
    jj_consume_token(AUTHOR);
    AuthorName();
  }

  final public void AuthorName() throws ParseException {
    jj_consume_token(QUOTE);
    AuthorLastName();
    jj_consume_token(COMMA);
    label_7:
    while (true) {
      AuthorFirstName();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SHORT_NAME:
      case LONG_NAME:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_7;
      }
    }
    jj_consume_token(QUOTE);
  }

  final public void AuthorLastName() throws ParseException {
    jj_consume_token(LONG_NAME);
  }

  final public void AuthorFirstName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SHORT_NAME:
      jj_consume_token(SHORT_NAME);
      break;
    case LONG_NAME:
      jj_consume_token(LONG_NAME);
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  /** Generated Token Manager. */
  public LibParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[14];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x421c00,0x421c00,0x0,0x10000,0x0,0x8000,0x3c0000,0x3c0000,0x800000,0x3000000,0x3000000,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x10,0x10000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x800,0x0,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x0,0x0,0x0,0x0,0x30,0x0,0x0,0x0,0x0,0x0,0x0,0x2,0xc,0xc,};
   }

  /** Constructor with InputStream. */
  public LibParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public LibParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new LibParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public LibParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new LibParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public LibParser(LibParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(LibParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[70];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 14; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 70; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
