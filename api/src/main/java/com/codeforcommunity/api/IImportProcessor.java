package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.imports.ImportBlocksRequest;
import com.codeforcommunity.dto.imports.ImportNeighborhoodsRequest;
import com.codeforcommunity.dto.imports.ImportReservationsRequest;
import com.codeforcommunity.dto.imports.ImportSitesRequest;
import com.codeforcommunity.dto.imports.ImportTreeBenefitsRequest;
import com.codeforcommunity.dto.imports.ImportTreeSpeciesRequest;

public interface IImportProcessor {
  void importBlocks(JWTData userData, ImportBlocksRequest importBlocksRequest);

  void importNeighborhoods(JWTData userData, ImportNeighborhoodsRequest importNeighborhoodsRequest);

  void importReservations(JWTData userData, ImportReservationsRequest importReservationsRequest);

  void importSites(JWTData userData, ImportSitesRequest importSitesRequest);

  void importTreeSpecies(JWTData userData, ImportTreeSpeciesRequest importTreeSpeciesRequest);

  void importTreeBenefits(JWTData userData, ImportTreeBenefitsRequest importTreeBenefitsRequest);
}
